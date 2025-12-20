import { Component, Inject, Input } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-modalerror',
  templateUrl: './modalerror.component.html',
  styleUrls: ['./modalerror.component.sass']
})
export class ModalerrorComponent {
  show = false;
  
  @Input() title: string | undefined;
  // @Input() data: any;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
  private dialogError: MatDialog) { }
  
  closeModal() {
    this.dialogError.closeAll();
  }
}
