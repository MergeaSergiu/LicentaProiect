import { Component, OnInit, ViewChild } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { response } from 'express';
import { error } from 'console';
import { UserDataResponse } from '../../models/user-response.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ClientService } from '../../services/client.service';
import { CollaborationResponse } from '../../models/collaboration-response.model';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { UtilComponentComponent } from '../../util-component/util-component.component';

@Component({
  selector: 'app-trainers',
  templateUrl: './trainers.component.html',
  styleUrl: './trainers.component.css'
})
export class TrainersComponent implements OnInit{

  constructor(private adminService: AdminService, private clientService: ClientService, private _responseBar: MatSnackBar){}
  trainers: any[];
  userCollaborations: CollaborationResponse[];
  displayedColumns: string[] = ['Trainer', 'Collab'];
  displayedColumns2: string[] = ['Trainer', 'Status'];
  dataSource: MatTableDataSource<any>;
  dataSource2: MatTableDataSource<any>;
  @ViewChild('paginator1') paginator1!: MatPaginator;
  @ViewChild('paginator2') paginator2!: MatPaginator;

  ngOnInit(): void {
    this.fetchAllTrainers();  
    this.getUserCollaboration();
  }

  Filterchange(data:Event){
    const value=(data.target as HTMLInputElement).value;
    this.dataSource.filter=value;
  }

  getUserCollaboration(){
    this.clientService.getCollaborationsForUser().subscribe({
        next: (response) => {
          console.log(response);
            this.userCollaborations = response;
            this.dataSource2 = new MatTableDataSource<any>(this.userCollaborations);
            this.dataSource2.paginator = this.paginator2;
        }
    })
  }


  public fetchAllTrainers(){
    this.adminService.getAllTrainers().subscribe({
      next: (response) => {
        console.log(response);
        this.trainers = response;
        this.dataSource = new MatTableDataSource<any>(this.trainers);
        this.dataSource.paginator = this.paginator1;
      }
    })
  }

  public collabWithTrainer(trainerId: number){
    this.clientService.sendCollabRequest(trainerId).subscribe({
      next: (response) => {
        this.getUserCollaboration();
        UtilComponentComponent.openSnackBar("Your request was sent", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
      },error: (error) => {
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  declineRequest(collaborationId: number){
    this.clientService.declineRequestForCollaboration(collaborationId).subscribe({
      next: (response) => {
        this.getUserCollaboration();
        UtilComponentComponent.openSnackBar("Your request was deleted", this._responseBar, UtilComponentComponent.SnackbarStates.Default);
      }
    })
  }

}
