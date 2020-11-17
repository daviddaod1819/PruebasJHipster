export interface ISpace {
  id?: number;
  title?: string;
  rooms?: number;
  meters?: number;
  price?: number;
  details?: string;
}

export class Space implements ISpace {
  constructor(
    public id?: number,
    public title?: string,
    public rooms?: number,
    public meters?: number,
    public price?: number,
    public details?: string
  ) {}
}
