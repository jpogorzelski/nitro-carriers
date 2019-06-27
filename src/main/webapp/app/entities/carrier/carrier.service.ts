import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICarrier } from 'app/shared/model/carrier.model';
import { IRating } from 'app/shared/model/rating.model';

type EntityResponseType = HttpResponse<ICarrier>;
type EntityArrayResponseType = HttpResponse<ICarrier[]>;

@Injectable({ providedIn: 'root' })
export class CarrierService {
    public resourceUrl = SERVER_API_URL + 'api/carriers';

    constructor(protected http: HttpClient) {}

    create(carrier: ICarrier): Observable<EntityResponseType> {
        return this.http.post<ICarrier>(this.resourceUrl, carrier, { observe: 'response' });
    }

    update(carrier: ICarrier): Observable<EntityResponseType> {
        return this.http.put<ICarrier>(this.resourceUrl, carrier, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICarrier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findRatings(id: number): Observable<HttpResponse<IRating[]>> {
        return this.http.get<IRating[]>(`${this.resourceUrl}/${id}/ratings`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICarrier[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
