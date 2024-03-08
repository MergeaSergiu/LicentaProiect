import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { TrainingClassResponse } from '../../models/trainingclass-response.model';
import { ClientService } from '../../services/client.service';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Component({
  selector: 'app-gym',
  templateUrl: './gym.component.html',
  styleUrl: './gym.component.css'
})
export class GymComponent implements OnInit{

  selectedTrainingClass: TrainingClassResponse;
  trainingClassesData: TrainingClassResponse[];
  userTrainingClassesData: TrainingClassResponse[];
  selectedTrainingClassId: number;
  successfulMessage: string;
  isEnrolled: boolean;
  panelOpenState = false;
  constructor(private adminService: AdminService, private clientService: ClientService,private _responseBar: MatSnackBar){}

  ngOnInit(): void{
    this.fetchTrainingClassesData();
    this.fetchUserTrainingClassesData();
  }

  selectionChangeHandler(event: any) {
    this.selectedTrainingClassId = event.value;
    this.fetchTrainingClassData(this.selectedTrainingClassId);
  }

  isEnrolledStatus(id: number){
    return this.checkEnrollmentStatus(id);
  }

  closeCard() {
    this.selectedTrainingClassId = null;
    this.selectedTrainingClass = null;
    this.isEnrolled = false;
  }

  fetchTrainingClassesData(){
    this.adminService.getTrainingClassesData().subscribe({
      next: (response) => {
        this.trainingClassesData = response;
        console.log(this.trainingClassesData);
      }
    })
  }

  fetchTrainingClassData(id: number){
    this.adminService.getTrainingClassData(id).subscribe({
      next:(response) => {
        this.selectedTrainingClass = response;
        this.checkEnrollmentStatus(id);
      }
    })
  }

  checkEnrollmentStatus(selectedTrainingClass: number){
    this.clientService.checkEnrollmentStatus(selectedTrainingClass).subscribe({
        next: (response) => {
          this.isEnrolled = response.status === "enrolled";
        }
      })
    }

  enrollUserToTrainingClass(classId: number){
    this.clientService.enrollUserToTrainingClass(classId).subscribe({
      next:(response) =>{
        this.fetchUserTrainingClassesData();
        this.isEnrolled = true;
        const config = new MatSnackBarConfig();
        config.verticalPosition = 'top'; // Set position to top
        this._responseBar.open("Congrats! You are enrolled", "",config);
        setTimeout(() => {
          this._responseBar.dismiss();
        }, 2000);
      }
    })
  }


  fetchUserTrainingClassesData(){
    this.clientService.getUserTrainingClasses().subscribe({
      next: (response) => {
        this.userTrainingClassesData = response;
      }
    })
  }

  dropOutOfTrainingClass(classId: number){
    this.clientService.unEnrolleUser(classId).subscribe({
      next: (response) =>{
        this.isEnrolled = false;
        this.fetchTrainingClassData(classId);
        this.fetchUserTrainingClassesData();
      }
    })
  }

}
