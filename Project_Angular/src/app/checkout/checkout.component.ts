import { Component, OnInit } from '@angular/core';
import { environment } from '../../environment';
import { PaymentData } from '../models/payment-data.model';
import { ClientService } from '../services/client.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UtilComponentComponent } from '../util-component/util-component.component';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {

  stripe = Stripe(environment.stripePublishableKey);
  elements: any;
  clientSecret: any;
  subscriptionId: number;
  paymentElement: any;
  subscriptionResponse: any;
  subscriptionForm: FormGroup;
  paymentData: PaymentData;

  constructor(private formBuilder: FormBuilder, private route: ActivatedRoute, private router: Router, private clientService: ClientService, private _responseBar: MatSnackBar) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.subscriptionId = params['subscId'];
    })
    this.fetchSubscriptionData(this.subscriptionId);
    this.subscriptionForm = this.formBuilder.group({
      cardholderName: ['', Validators.required],
    });
    this.setUpStripePaymentForm();
  }

  goToGymPage() {
    this.router.navigate(['/client/gym'])
  }

  fetchSubscriptionData(subscriptionId: number) {
    this.clientService.getSubscriptionById(subscriptionId).subscribe({
      next: (response: any) => {
        this.subscriptionResponse = response;
      }
    })
  }

  setUpStripePaymentForm() {
    this.elements = this.stripe.elements();
    this.paymentElement = this.elements.create("card");
    this.paymentElement.mount("#card-element");
  }


  async onSubmit(form: NgForm) {

    this.paymentData = {
      cardHolderName: form.value.cardHolderName,
      amount: this.subscriptionResponse.subscriptionPrice * 100,
      currency: "ron"
    }
    try {
      this.clientService.createPaymentIntent(this.paymentData).subscribe({
        next: (response: any) => {
          this.stripe.confirmCardPayment(
            response.client_secret,
            {
              payment_method: {
                card: this.paymentElement
              }
            }, {
            handleActions: false
          }).then((result: any) => {
            if (result.error) {
              UtilComponentComponent.openSnackBar(result.error.message, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
              form.reset();
            } else {
              this.clientService.AddUserSubscriptionByCard(this.subscriptionId).subscribe({
                next: (response: any) => {
                  UtilComponentComponent.openSnackBar("Payment was succesful", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
                  setTimeout(() => {
                    this.router.navigate(['/client/account']);
                  }, 1200);
                }, error: (error: any) => {
                  UtilComponentComponent.openSnackBar("We can not register your subscription", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
                }
              })
            }
          })
        },
        error: (error) => {
          UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
        }
      })
    } catch (error) {
      UtilComponentComponent.openSnackBar("Error handling payment and saving subscription data", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
    }
  }
}
