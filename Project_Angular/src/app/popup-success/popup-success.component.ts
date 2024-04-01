import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-popup-success',
  templateUrl: './popup-success.component.html',
  styleUrl: './popup-success.component.css'
})
export class PopupSuccessComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: any){}
  message: any;
  ngOnInit(): void{
    
   this.message = this.data.message;
  }
}
