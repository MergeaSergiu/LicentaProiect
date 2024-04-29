import { Component, OnInit, ViewChild } from '@angular/core';
import { ClientService } from '../services/client.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { CollaborationResponse } from '../models/collaboration-response.model';
import { UtilComponentComponent } from '../util-component/util-component.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-trainer',
  templateUrl: './trainer.component.html',
  styleUrl: './trainer.component.css'
})
export class TrainerComponent implements OnInit{
  
  constructor(private clientService: ClientService, private _responseBar: MatSnackBar){}
  
  collaborations: CollaborationResponse[];
  displayedColumns: string[] = ['User Name','Status','Period'];
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
          UtilComponentComponent.openSnackBar("You accepted the request", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
          this.getCollaborationForTrainer();
      }
    })
  }

  public declineRequest(collaborationId: number) {
    this.clientService.declineRequestForCollaboration(collaborationId).subscribe({
      next: (response: any) => {
        this.getCollaborationForTrainer();
        UtilComponentComponent.openSnackBar("You declined the request", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  public finishCollaboration(collaborationId: number){
    this.clientService.finishCollaborationWithUser(collaborationId).subscribe({
      next: (response) => {
        UtilComponentComponent.openSnackBar("You set the collaboration as finished", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
        this.getCollaborationForTrainer();
      }
    })
  }

  
}

