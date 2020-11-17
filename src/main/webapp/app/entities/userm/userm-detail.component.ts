import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserm } from 'app/shared/model/userm.model';

@Component({
  selector: 'jhi-userm-detail',
  templateUrl: './userm-detail.component.html',
})
export class UsermDetailComponent implements OnInit {
  userm: IUserm | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userm }) => (this.userm = userm));
  }

  previousState(): void {
    window.history.back();
  }
}
