import { Component, OnInit } from '@angular/core';
import { environment } from '../../environment';
import { PaymentData} from '../models/payment-data.model';
import { ClientService } from '../services/client.service';
import { AdminService } from '../services/admin.service';
import { SubscriptionRequest } from '../models/subscription-request.model';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { PopupSuccessComponent } from '../popup-success/popup-success.component';

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
  displayError: any;
  subscriptionResponse: any;
  subscriptionForm: FormGroup;

  constructor(private dialog: MatDialog,private route: ActivatedRoute,private router: Router, private clientService: ClientService, private adminService: AdminService){}

  ngOnInit(): void{
    this.route.queryParams.subscribe(params => {
      this.subscriptionId = params['subscId'];
    })
    this.fetchSubscriptionData(this.subscriptionId);
    this.subscriptionForm = new FormGroup({});
    this.setUpStripePaymentForm();
  }

  fetchSubscriptionData(subscriptionId: number) {
    this.clientService.getSubscriptionById(subscriptionId).subscribe({
      next: (response: any) => {
          this.subscriptionResponse = response;
      }
    })
  }
  
  setUpStripePaymentForm(){
    
    const style = {
      base: {
        color: '#32325d',
        fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
        fontSmoothing: 'antialiased',
        fontSize: '16px',
        '::placeholder': {
          color: '#aab7c4'
        }
      },
      invalid: {
        color: '#fa755a',
        iconColor: '#fa755a'
      }
    };
         this.elements = this.stripe.elements({ style});
       
         
          this.paymentElement = this.elements.create("card");
         this.paymentElement.mount("#card-element");
      }


  async onSubmit() {

    const paymentData = {
      amount: 10000,
      currency : "ron"
    }
    try {
      this.clientService.createPaymentIntent(paymentData).subscribe({
        next: (response: any) => {  
          if(response.status === 'requires_confirmation'){
            
          this.stripe.confirmCardPayment(
              response.client_secret,
              {payment_method: {
                card: this.paymentElement
              }
            }, {handleActions: false}).then((result: any) => {
              if(result.error) {
                alert(result.error.message)
              }else{
                
                this.adminService.AddUserSubscriptionByCard(this.subscriptionId).subscribe({
                  next: (response: any) =>{
                    var _resultPopUp
                  },error: (error: any) => {
                    console.log("Nu merge");
                  }
              })
              var _popUpMessage = this.dialog.open(PopupSuccessComponent, {
                width: '50%',
                enterAnimationDuration: '400ms',
                exitAnimationDuration: '400ms',
                data: {
                  message: 'Payment successful!'
                }
              });
              setTimeout(() => {
                _popUpMessage.close();
                this.router.navigate(['/client/account']);
              }, 1500);
              }
            })
        } else{
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
            
          }, 1500);
        } 
      }
      })
      // Reset form or perform other actions after successful submission
    } catch (error) {
      console.error('Error handling payment and saving subscription data:', error);
      // Handle error
    }
  }
  }
