import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MdButtonModule,
  MdCardModule,
  MdCheckboxModule,
  MdDialogModule,
  MdExpansionModule,
  MdIconModule,
  MdInputModule,
  MdListModule, MdMenuModule,
  MdProgressSpinnerModule,
  MdSelectModule,
  MdToolbarModule
} from '@angular/material';

export const MATERIAL_DESIGN = [
  MdButtonModule,
  MdDialogModule,
  MdIconModule,
  MdInputModule,
  MdListModule,
  MdToolbarModule,
  MdCardModule,
  MdProgressSpinnerModule,
  MdSelectModule,
  MdCheckboxModule,
  MdExpansionModule,
  MdMenuModule
];

@NgModule({
  imports: [
    CommonModule,
    MATERIAL_DESIGN
  ],
  exports: [
    MATERIAL_DESIGN
  ]
})
export class AppMaterialModule {
}
