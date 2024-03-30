import { Component, OnInit, ViewChild } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { response } from 'express';
import { error } from 'console';
import { UserDataResponse } from '../../models/user-response.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-trainers',
  templateUrl: './trainers.component.html',
  styleUrl: './trainers.component.css'
})
export class TrainersComponent implements OnInit{

  constructor(private adminService: AdminService, private clientService: ClientService){}
  trainers: any[];
  userCollaborations: any[];
  displayedColumns: string[] = ['Trainer', 'Collab'];
  displayedColumns2: string[] = ['Trainer', 'Status'];
  dataSource: MatTableDataSource<any>;
  dataSource2: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatPaginator) paginator2!: MatPaginator;

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
        this.dataSource.paginator = this.paginator;
      }
    })
  }

  public collabWithTrainer(trainerId: number){
    this.clientService.sendCollabRequest(trainerId).subscribe({
      next: (response) => {
        this.getUserCollaboration();
      },error: (error) => {
        alert(error);
      }
    })
  }

  declineRequest(collaborationId: number){
    this.clientService.declineRequestForCollaboration(collaborationId).subscribe({
      next: (response) => {
        this.getUserCollaboration();
      }
    })
  }

}
