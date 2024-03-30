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
  displayedColumns: string[] = ['Name', 'Collab'];
  sentRequest: number = 0;
  dataSource: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngOnInit(): void {
    this.fetchAllTrainers();  
  }

  Filterchange(data:Event){
    const value=(data.target as HTMLInputElement).value;
    this.dataSource.filter=value;
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
        this.sentRequest = 1;
      },error: (error) => {
        alert(error);
      }
    })
  }

}
