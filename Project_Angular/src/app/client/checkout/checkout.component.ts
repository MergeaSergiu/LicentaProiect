import { Component, OnInit } from '@angular/core';
import { environment } from '../../../environment';
import { PaymentData } from '../../models/payment-data.model';
import { ClientService } from '../../services/client.service';
import { Router } from '@angular/router';
import { FormGroup, NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UtilComponentComponent } from '../../util-component/util-component.component';
import { SubscriptionResponse } from '../../models/subscription-response.model';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {

  stripe = Stripe(environment.stripePublishableKey);
  elements: any;
  price: number;
  clientSecret: any;
  paymentElement: any;
  subscriptionId: number;
  subscriptionResponse: SubscriptionResponse;
  subscriptionForm: FormGroup;
  paymentData: PaymentData;

  constructor(private clientService: ClientService, private router: Router, private _responseBar: MatSnackBar) { }

  ngOnInit(): void {
    this.price = parseFloat(history.state.price);
    this.subscriptionId = parseInt(history.state.subscriptionId);
    this.setUpStripePaymentForm();
  }

  setUpStripePaymentForm() {
    this.elements = this.stripe.elements();
    this.paymentElement = this.elements.create("card");
    this.paymentElement.mount("#card-element");
  }


  async onSubmit(form: NgForm) {

    this.paymentData = {
      cardHolderName: form.value.cardHolderName,
      amount: this.price * 100,
      currency: "ron",
      subscriptionId: this.subscriptionId
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
                next: () => {
                  UtilComponentComponent.openSnackBar("Payment was succesful", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
                  setTimeout(() => {
                    this.router.navigate(['/client/account']);
                  }, 1200);
                }, error: (error: any) => {
                  UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
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
