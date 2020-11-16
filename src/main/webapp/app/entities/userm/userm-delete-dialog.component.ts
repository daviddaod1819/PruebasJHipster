import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserm } from 'app/shared/model/userm.model';
import { UsermService } from './userm.service';

@Component({
  templateUrl: './userm-delete-dialog.component.html',
})
export class UsermDeleteDialogComponent {
  userm?: IUserm;

  constructor(protected usermService: UsermService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.usermService.delete(id).subscribe(() => {
      this.eventManager.broadcast('usermListModification');
      this.activeModal.close();
    });
  }
}
