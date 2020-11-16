import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUserm } from 'app/shared/model/userm.model';

type EntityResponseType = HttpResponse<IUserm>;
type EntityArrayResponseType = HttpResponse<IUserm[]>;

@Injectable({ providedIn: 'root' })
export class UsermService {
  public resourceUrl = SERVER_API_URL + 'api/userms';

  constructor(protected http: HttpClient) {}

  create(userm: IUserm): Observable<EntityResponseType> {
    return this.http.post<IUserm>(this.resourceUrl, userm, { observe: 'response' });
  }

  update(userm: IUserm): Observable<EntityResponseType> {
    return this.http.put<IUserm>(this.resourceUrl, userm, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserm>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserm[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
