import { DialogsService } from '../../service/dialog.service';
import { MdButtonModule, MdDialogModule } from '@angular/material';
import { NgModule } from '@angular/core';

import { ConfirmDialog } from './confirm-dialog';
import { ContentDialog } from './content-dialog';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  imports: [
    MdDialogModule,
    MdButtonModule,
    TranslateModule
  ],
  exports: [
    ConfirmDialog,
    ContentDialog
  ],
  declarations: [
    ConfirmDialog,
    ContentDialog
  ],
  providers: [
    DialogsService,
  ],
  entryComponents: [
    ConfirmDialog,
    ContentDialog
  ],
})

export class DialogsModule {
}
