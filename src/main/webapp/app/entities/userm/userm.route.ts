import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IUserm, Userm } from 'app/shared/model/userm.model';
import { UsermService } from './userm.service';
import { UsermComponent } from './userm.component';
import { UsermDetailComponent } from './userm-detail.component';
import { UsermUpdateComponent } from './userm-update.component';

@Injectable({ providedIn: 'root' })
export class UsermResolve implements Resolve<IUserm> {
  constructor(private service: UsermService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserm> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((userm: HttpResponse<Userm>) => {
          if (userm.body) {
            return of(userm.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Userm());
  }
}

export const usermRoute: Routes = [
  {
    path: '',
    component: UsermComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Userms',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UsermDetailComponent,
    resolve: {
      userm: UsermResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Userms',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UsermUpdateComponent,
    resolve: {
      userm: UsermResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Userms',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UsermUpdateComponent,
    resolve: {
      userm: UsermResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Userms',
    },
    canActivate: [UserRouteAccessService],
  },
];
