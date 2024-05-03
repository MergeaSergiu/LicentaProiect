import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { AdminService } from '../services/admin.service';
import { NgForm } from '@angular/forms';
import { TrainingClassRequest } from '../models/trainingclass-request.model';
import { TrainerDataResponse } from '../models/trainers-response.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UtilComponentComponent } from '../util-component/util-component.component';

@Component({
  selector: 'app-popup-create-tr-class',
  templateUrl: './popup-create-tr-class.component.html',
  styleUrl: './popup-create-tr-class.component.css'
})
export class PopupCreateTrClassComponent {

  selectedTrainerId: number;
  trainersData: TrainerDataResponse[];
  constructor(private matDialog: MatDialogRef<PopupCreateTrClassComponent>, private adminService: AdminService,private _responseBar: MatSnackBar){}

  closePopUp(){
    this.matDialog.close();
  }

  ngOnInit(): void{
    this.fetchTrainersData();
  }

  public fetchTrainersData(){
    return this.adminService.getAllTrainers().subscribe(
      response => {
        this.trainersData = response;
      }
    )
  }

  onSubmitCreateTrainingClass(form: NgForm){

    const trainingClass: TrainingClassRequest = {
          className: form.value.className,
          duration: form.value.duration,
          startTime: form.value.startTime,
          intensity: form.value.intensity,
          localDate: form.value.localDate,
          trainerId: form.value.trainerId
    }

    this.adminService.createTrainingClass(trainingClass).subscribe({
      next: () => {
        this.closePopUp();
      }, error: (error: any) =>{
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    });
  }

  
}
