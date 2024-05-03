import { Component } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Component({
  selector: 'app-util-component',
  templateUrl: './util-component.component.html',
  styleUrl: './util-component.component.css'
})
export class UtilComponentComponent {

  constructor(){}
  static SnackbarStates = { 
    Success: "success",
    Error: "error",
}
  
  static openSnackBar(message: string, snackBar: MatSnackBar, status: string){
    snackBar.open(
      message, 
      '', 
      {
      duration: 2000,
      verticalPosition: 'bottom',
      panelClass: status == this.SnackbarStates.Success ? 'app-notification-success' : 'app-notification-error'
      }
    );
  }
}
