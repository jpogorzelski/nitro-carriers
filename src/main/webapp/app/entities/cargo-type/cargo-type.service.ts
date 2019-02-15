import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICargoType } from 'app/shared/model/cargo-type.model';

type EntityResponseType = HttpResponse<ICargoType>;
type EntityArrayResponseType = HttpResponse<ICargoType[]>;

@Injectable({ providedIn: 'root' })
export class CargoTypeService {
    public resourceUrl = SERVER_API_URL + 'api/cargo-types';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/cargo-types';

    constructor(protected http: HttpClient) {}

    create(cargoType: ICargoType): Observable<EntityResponseType> {
        return this.http.post<ICargoType>(this.resourceUrl, cargoType, { observe: 'response' });
    }

    update(cargoType: ICargoType): Observable<EntityResponseType> {
        return this.http.put<ICargoType>(this.resourceUrl, cargoType, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICargoType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICargoType[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICargoType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
