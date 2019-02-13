import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IRating } from 'app/shared/model/rating.model';

type EntityResponseType = HttpResponse<IRating>;
type EntityArrayResponseType = HttpResponse<IRating[]>;

@Injectable({ providedIn: 'root' })
export class NitroRatingService {
    public resourceUrl = SERVER_API_URL + 'api/ext/ratings';

    constructor(protected http: HttpClient) {}

    create(rating: IRating): Observable<EntityResponseType> {
        return this.http.post<IRating>(this.resourceUrl, rating, { observe: 'response' });
    }

    update(rating: IRating): Observable<EntityResponseType> {
        return this.http.put<IRating>(this.resourceUrl, rating, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRating>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
