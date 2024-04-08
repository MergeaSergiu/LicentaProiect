import { Component, OnInit } from '@angular/core';
import { environment } from '../../environment';
import { PaymentData} from '../models/payment-data.model';
import { ClientService } from '../services/client.service';
import { AdminService } from '../services/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { PopupSuccessComponent } from '../popup-success/popup-success.component';
import { error } from 'console';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit{

  stripe = Stripe(environment.stripePublishableKey);
  elements: any;
  clientSecret: any;
  subscriptionId: number;
  paymentElement: any;
  subscriptionResponse: any;
  subscriptionForm: FormGroup;
  paymentData: PaymentData;

  constructor(private formBuilder: FormBuilder,private dialog: MatDialog,private route: ActivatedRoute,private router: Router, private clientService: ClientService){}

  ngOnInit(): void{
    this.route.queryParams.subscribe(params => {
      this.subscriptionId = params['subscId'];
    })
    this.fetchSubscriptionData(this.subscriptionId);
    this.subscriptionForm = this.formBuilder.group({
      cardholderName: ['', Validators.required],
    });
    this.setUpStripePaymentForm();
  }

  goToGymPage(){
    this.router.navigate(['/client/gym'])
  }

  fetchSubscriptionData(subscriptionId: number) {
    this.clientService.getSubscriptionById(subscriptionId).subscribe({
      next: (response: any) => {
          this.subscriptionResponse = response;
      }
    })
  }
  
  setUpStripePaymentForm(){
    
    // const style = {
    //   base: {
    //     color: '#ffffff',
    //     fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
    //     fontSmoothing: 'antialiased',
    //     fontSize: '100px',
    //     '::placeholder': {
    //       color: '#aab7c4'
    //     }
    //   },
    //   invalid: {
    //     color: '#fa755a',
    //     iconColor: '#fa755a'
    //   }
    // };
        this.elements = this.stripe.elements();
        this.paymentElement = this.elements.create("card");
        this.paymentElement.mount("#card-element");
      }


  async onSubmit(form: NgForm) {

    this.paymentData = {
      cardHolderName : form.value.cardHolderName,
      amount: this.subscriptionResponse.subscriptionPrice * 100,
      currency : "ron"
    }
    try {

      this.clientService.checkUserActiveSubscriptions().subscribe({
        next: (response) => {
          if(response.hasActiveSubscription == false){
            this.clientService.createPaymentIntent(this.paymentData).subscribe({
              next: (response: any) => {  
                this.stripe.confirmCardPayment(
                    response.client_secret,
                    {payment_method: {
                      card: this.paymentElement
                    }
                  }, {
                    handleActions: false
                  }).then((result: any) => {
                    if(result.error) {
                      var _popUpMessage = this.dialog.open(PopupSuccessComponent, {
                        width: '50%',
                        enterAnimationDuration: '400ms',
                        exitAnimationDuration: '400ms',
                        data: {
                          message: 'Payment failed!'
                        }
                      });
                      setTimeout(() => {
                        _popUpMessage.close();
                      }, 1200);
                    }else{
                      this.clientService.AddUserSubscriptionByCard(this.subscriptionId).subscribe({
                        next: (response: any) =>{
                          var _popUpMessage = this.dialog.open(PopupSuccessComponent, {
                            enterAnimationDuration: '400ms',
                            exitAnimationDuration: '400ms',
                            data: {
                              message: 'Payment successful!'
                            }
                          });
                          setTimeout(() => {
                            _popUpMessage.close();
                            this.router.navigate(['/client/account']);
                          }, 1200);
                        },error: (error: any) => {
                          alert('We can not register your subscription')
                        }
                      })
                    }
                  })
              }
            })
          }else{
            var _popUpMessage = this.dialog.open(PopupSuccessComponent, {
              enterAnimationDuration: '400ms',
              exitAnimationDuration: '400ms',
              data: {
                message: 'You already have an active subscription. We can not proced your payment'
              }
            });
            setTimeout(() => {
              _popUpMessage.close();
            }, 1200);
          }
        }
       });


      // Reset form or perform other actions after successful submission
    } catch (error) {
      alert('Error handling payment and saving subscription data:');
      // Handle error
    }
  }
  }
