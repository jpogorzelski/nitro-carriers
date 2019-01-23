export interface ICargoType {
    id?: number;
    name?: string;
}

export class CargoType implements ICargoType {
    constructor(public id?: number, public name?: string) {}
}
