import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IUserInfo, UserInfo } from 'app/shared/model/user-info.model';
import { UserInfoService } from './user-info.service';

@Component({
  selector: 'jhi-user-info-update',
  templateUrl: './user-info-update.component.html',
})
export class UserInfoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sex: [],
    birthDate: [null, [Validators.required]],
    country: [null, [Validators.minLength(1), Validators.maxLength(50)]],
    town: [null, [Validators.minLength(1), Validators.maxLength(50)]],
    postCode: [null, [Validators.minLength(5), Validators.maxLength(5)]],
  });

  constructor(protected userInfoService: UserInfoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userInfo }) => {
      if (!userInfo.id) {
        const today = moment().startOf('day');
        userInfo.birthDate = today;
      }

      this.updateForm(userInfo);
    });
  }

  updateForm(userInfo: IUserInfo): void {
    this.editForm.patchValue({
      id: userInfo.id,
      sex: userInfo.sex,
      birthDate: userInfo.birthDate ? userInfo.birthDate.format(DATE_TIME_FORMAT) : null,
      country: userInfo.country,
      town: userInfo.town,
      postCode: userInfo.postCode,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userInfo = this.createFromForm();
    if (userInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.userInfoService.update(userInfo));
    } else {
      this.subscribeToSaveResponse(this.userInfoService.create(userInfo));
    }
  }

  private createFromForm(): IUserInfo {
    return {
      ...new UserInfo(),
      id: this.editForm.get(['id'])!.value,
      sex: this.editForm.get(['sex'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value ? moment(this.editForm.get(['birthDate'])!.value, DATE_TIME_FORMAT) : undefined,
      country: this.editForm.get(['country'])!.value,
      town: this.editForm.get(['town'])!.value,
      postCode: this.editForm.get(['postCode'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserInfo>>): void {
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
