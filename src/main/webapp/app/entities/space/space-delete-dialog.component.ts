import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISpace } from 'app/shared/model/space.model';
import { SpaceService } from './space.service';

@Component({
  templateUrl: './space-delete-dialog.component.html',
})
export class SpaceDeleteDialogComponent {
  space?: ISpace;

  constructor(protected spaceService: SpaceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.spaceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('spaceListModification');
      this.activeModal.close();
    });
  }
}
