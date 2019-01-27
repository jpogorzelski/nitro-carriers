import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { INitroRating } from 'app/shared/model/nitro-rating.model';

type EntityResponseType = HttpResponse<INitroRating>;
type EntityArrayResponseType = HttpResponse<INitroRating[]>;

@Injectable({ providedIn: 'root' })
export class NitroRatingService {
    public resourceUrl = SERVER_API_URL + 'api/ext/ratings';

    constructor(protected http: HttpClient) {}

    create(rating: INitroRating): Observable<EntityResponseType> {
        return this.http.post<INitroRating>(this.resourceUrl, rating, { observe: 'response' });
    }

    update(rating: INitroRating): Observable<EntityResponseType> {
        return this.http.put<INitroRating>(this.resourceUrl, rating, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<INitroRating>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
