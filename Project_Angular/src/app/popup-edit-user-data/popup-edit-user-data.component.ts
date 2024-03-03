import { Component, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AdminService } from '../services/admin.service';
import { UserDataResponse } from '../models/user-response.model';
import { Role } from '../models/role.model';
import { NgForm } from '@angular/forms';
import { RoleRequest } from '../models/role-request.model';
import { response } from 'express';

@Component({
  selector: 'app-popup-edit-user-data',
  templateUrl: './popup-edit-user-data.component.html',
  styleUrl: './popup-edit-user-data.component.css'
})
export class PopupEditUserDataComponent {

  @ViewChild('authForm') authForm: NgForm;
  selectedDate: any;
  editData: UserDataResponse;
  inputData: any;
  selectedRoleId: number;
  roles: Role[] = [
    {id: 1, name: 'USER'},
    {id: 2, name: 'ADMIN'},
    {id: 3, name: 'TRAINER'}
  ];
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private matDialog: MatDialogRef<PopupEditUserDataComponent>, private adminService: AdminService){}

  closePopUp(){
    this.matDialog.close();
  }

  ngOnInit(): void{
    this.inputData = this.data;
    this.setPopUpData(this.inputData.id)
  }

  setPopUpData(id: number){
    this.adminService.getUser(id).subscribe(
      response => {
        this.editData= response;
       if (this.authForm) {
        this.authForm.form.patchValue({
          roleId: this.editData.role.id
        });
      }
    })
  }

  OnSubmitEditUserRole(form: NgForm){

    const roleRequest: RoleRequest = {
      id: form.value.roleId
    }

    this.adminService.updateUserRole(this.inputData.id, roleRequest).subscribe({
      next: (response) =>{
        this.closePopUp();
      }, error: (any) =>{
        alert("Can not update User Role");
      }});
      
  }


}
