import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PruebasJHipsterSharedModule } from 'app/shared/shared.module';
import { UsermComponent } from './userm.component';
import { UsermDetailComponent } from './userm-detail.component';
import { UsermUpdateComponent } from './userm-update.component';
import { UsermDeleteDialogComponent } from './userm-delete-dialog.component';
import { usermRoute } from './userm.route';

@NgModule({
  imports: [PruebasJHipsterSharedModule, RouterModule.forChild(usermRoute)],
  declarations: [UsermComponent, UsermDetailComponent, UsermUpdateComponent, UsermDeleteDialogComponent],
  entryComponents: [UsermDeleteDialogComponent],
})
export class PruebasJHipsterUsermModule {}
