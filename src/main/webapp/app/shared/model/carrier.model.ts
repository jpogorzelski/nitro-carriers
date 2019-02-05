import { IPerson } from 'app/shared/model/person.model';
import { IRating } from 'app/shared/model/rating.model';

export interface ICarrier {
    id?: number;
    name?: string;
    transId?: number;
    people?: IPerson[];
    ratings?: IRating[];
}

export class Carrier implements ICarrier {
    constructor(public id?: number, public name?: string, public transId?: number, public people?: IPerson[], public ratings?: IRating[]) {}
}
