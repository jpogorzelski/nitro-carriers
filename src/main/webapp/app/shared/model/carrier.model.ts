import { IPerson } from 'app/shared/model/person.model';
import { IRating } from 'app/shared/model/rating.model';

export interface ICarrier {
    id?: number;
    name?: string;
    transId?: number;
    acronym?: string;
    nip?: string;
    people?: IPerson[];
    ratings?: IRating[];
}

export class Carrier implements ICarrier {
    constructor(
        public id?: number,
        public name?: string,
        public transId?: number,
        public acronym?: string,
        public nip?: string,
        public people?: IPerson[],
        public ratings?: IRating[]
    ) {}
}
