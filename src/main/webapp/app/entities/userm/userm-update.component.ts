import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IUserm, Userm } from 'app/shared/model/userm.model';
import { UsermService } from './userm.service';
import { IUserInfo } from 'app/shared/model/user-info.model';
import { UserInfoService } from 'app/entities/user-info/user-info.service';

@Component({
  selector: 'jhi-userm-update',
  templateUrl: './userm-update.component.html',
})
export class UsermUpdateComponent implements OnInit {
  isSaving = false;
  userinfos: IUserInfo[] = [];

  editForm = this.fb.group({
    id: [],
    userInfoId: [],
  });

  constructor(
    protected usermService: UsermService,
    protected userInfoService: UserInfoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userm }) => {
      this.updateForm(userm);

      this.userInfoService
        .query({ 'userId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IUserInfo[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IUserInfo[]) => {
          if (!userm.userInfoId) {
            this.userinfos = resBody;
          } else {
            this.userInfoService
              .find(userm.userInfoId)
              .pipe(
                map((subRes: HttpResponse<IUserInfo>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IUserInfo[]) => (this.userinfos = concatRes));
          }
        });
    });
  }

  updateForm(userm: IUserm): void {
    this.editForm.patchValue({
      id: userm.id,
      userInfoId: userm.userInfoId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userm = this.createFromForm();
    if (userm.id !== undefined) {
      this.subscribeToSaveResponse(this.usermService.update(userm));
    } else {
      this.subscribeToSaveResponse(this.usermService.create(userm));
    }
  }

  private createFromForm(): IUserm {
    return {
      ...new Userm(),
      id: this.editForm.get(['id'])!.value,
      userInfoId: this.editForm.get(['userInfoId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserm>>): void {
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

  trackById(index: number, item: IUserInfo): any {
    return item.id;
  }
}
