import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISpace, Space } from 'app/shared/model/space.model';
import { SpaceService } from './space.service';
import { SpaceComponent } from './space.component';
import { SpaceDetailComponent } from './space-detail.component';
import { SpaceUpdateComponent } from './space-update.component';

@Injectable({ providedIn: 'root' })
export class SpaceResolve implements Resolve<ISpace> {
  constructor(private service: SpaceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpace> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((space: HttpResponse<Space>) => {
          if (space.body) {
            return of(space.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Space());
  }
}

export const spaceRoute: Routes = [
  {
    path: '',
    component: SpaceComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Spaces',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpaceDetailComponent,
    resolve: {
      space: SpaceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Spaces',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpaceUpdateComponent,
    resolve: {
      space: SpaceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Spaces',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpaceUpdateComponent,
    resolve: {
      space: SpaceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Spaces',
    },
    canActivate: [UserRouteAccessService],
  },
];
