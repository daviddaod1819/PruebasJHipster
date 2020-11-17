import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ISpace } from 'app/shared/model/space.model';

type EntityResponseType = HttpResponse<ISpace>;
type EntityArrayResponseType = HttpResponse<ISpace[]>;

@Injectable({ providedIn: 'root' })
export class SpaceService {
  public resourceUrl = SERVER_API_URL + 'api/spaces';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/spaces';

  constructor(protected http: HttpClient) {}

  create(space: ISpace): Observable<EntityResponseType> {
    return this.http.post<ISpace>(this.resourceUrl, space, { observe: 'response' });
  }

  update(space: ISpace): Observable<EntityResponseType> {
    return this.http.put<ISpace>(this.resourceUrl, space, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISpace>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpace[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpace[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
