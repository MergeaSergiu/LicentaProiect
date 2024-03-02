import { Component, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AdminService } from '../services/admin.service';
import { NgForm } from '@angular/forms';
import { TrainerDataResponse } from '../models/trainers-response.model';
import { TrainingClassResponse } from '../models/trainingclass-response.model';
import { TrainingClassRequest } from '../models/trainingclass-request.model';

@Component({
  selector: 'app-popup-edit-tr-class',
  templateUrl: './popup-edit-tr-class.component.html',
  styleUrl: './popup-edit-tr-class.component.css'
})
export class PopupEditTrClassComponent {

  @ViewChild('authForm') authForm: NgForm;
  selectedDate: any;
  editData: TrainingClassResponse;
  inputData: any;
  selectedTrainerId: number;
  trainersData: TrainerDataResponse[];
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private matDialog: MatDialogRef<PopupEditTrClassComponent>, private adminService: AdminService){}

  closePopUp(){
    this.matDialog.close();
  }

  ngOnInit(): void{
    this.inputData = this.data;
    this.setPopUpData(this.inputData.id)
  }

  public fetchTrainersData(){
    return this.adminService.getAllTrainers().subscribe(
      response => {
        this.trainersData = response;
      }
    )
  }

  setPopUpData(id: number){
    this.fetchTrainersData();
    this.adminService.getTrainingClassData(id).subscribe(
      response => {
        this.editData= response;
       if (this.authForm) {
        this.authForm.form.patchValue({
          className: this.editData.className,
          duration: this.editData.duration,
          intensity: this.editData.intensity,
          localDate: this.editData.localDate,
          startTime: this.editData.startTime,
          trainerId: this.editData.trainerId
        });
      }
    })
  }

  OnSubmitUpdateClassData(form: NgForm){

    const trainingClass: TrainingClassRequest = {
        className: form.value.className,
        duration: form.value.duration,
        startTime: form.value.startTime,
        intensity: form.value.intensity,
        localDate: form.value.localDate,
        trainerId: form.value.trainerId
  }

  this.adminService.updateTrainingClass(this.inputData.id, trainingClass).subscribe({
      next: (response) =>{
        this.closePopUp();
      }, error : (any) =>{
        alert("Can not update Training Class Data");
      }
    });
  }
}
