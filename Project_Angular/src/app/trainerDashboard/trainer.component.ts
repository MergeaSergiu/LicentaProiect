import { Component, OnInit, ViewChild } from '@angular/core';
import { ClientService } from '../services/client.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-trainer',
  templateUrl: './trainer.component.html',
  styleUrl: './trainer.component.css'
})
export class TrainerComponent implements OnInit{
  
  constructor(private clientService: ClientService){}
  
  collaborations: any[];
  displayedColumns: string[] = ['User Name', 'Status'];
  dataSource: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngOnInit(): void {
    this.getCollaborationForTrainer();
  }

  Filterchange(data:Event){
    const value=(data.target as HTMLInputElement).value;
    this.dataSource.filter=value;
  }

  public getCollaborationForTrainer(){
    this.clientService.getTrainerCollaborations().subscribe({
      next: (response: any) => {
        this.collaborations = response;
        this.dataSource = new MatTableDataSource<any>(this.collaborations);
        this.dataSource.paginator = this.paginator;
      }
    })
  }

  public acceptRequest(collaborationId: number) {
    this.clientService.acceptRequestForCollaboration(collaborationId).subscribe({
      next: (response: any) => {
          this.getCollaborationForTrainer();
      }
    })
  }

  public declineRequest(collaborationId: number) {
    this.clientService.declineRequestForCollaboration(collaborationId).subscribe({
      next: (response: any) => {
        this.getCollaborationForTrainer();
      }
    })
  }

  public finishCollaboration(collaborationId: number){
    this.clientService.finishCollaborationWithUser(collaborationId).subscribe({
      next: (response) => {
        this.getCollaborationForTrainer();
      }
    })
  }

  
}

