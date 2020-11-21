import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ISpace, Space } from 'app/shared/model/space.model';
import { SpaceService } from './space.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-space-update',
  templateUrl: './space-update.component.html',
})
export class SpaceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.minLength(1), Validators.maxLength(50)]],
    rooms: [],
    meters: [],
    price: [],
    details: [],
    image: [],
    imageContentType: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected spaceService: SpaceService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ space }) => {
      this.updateForm(space);
    });
  }

  updateForm(space: ISpace): void {
    this.editForm.patchValue({
      id: space.id,
      title: space.title,
      rooms: space.rooms,
      meters: space.meters,
      price: space.price,
      details: space.details,
      image: space.image,
      imageContentType: space.imageContentType,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('pruebasJHipsterApp.error', { message: err.message })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const space = this.createFromForm();
    if (space.id !== undefined) {
      this.subscribeToSaveResponse(this.spaceService.update(space));
    } else {
      this.subscribeToSaveResponse(this.spaceService.create(space));
    }
  }

  private createFromForm(): ISpace {
    return {
      ...new Space(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      rooms: this.editForm.get(['rooms'])!.value,
      meters: this.editForm.get(['meters'])!.value,
      price: this.editForm.get(['price'])!.value,
      details: this.editForm.get(['details'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpace>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
