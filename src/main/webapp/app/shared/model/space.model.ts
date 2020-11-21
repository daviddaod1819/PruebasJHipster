export interface ISpace {
  id?: number;
  title?: string;
  rooms?: number;
  meters?: number;
  price?: number;
  details?: string;
  imageContentType?: string;
  image?: any;
}

export class Space implements ISpace {
  constructor(
    public id?: number,
    public title?: string,
    public rooms?: number,
    public meters?: number,
    public price?: number,
    public details?: string,
    public imageContentType?: string,
    public image?: any
  ) {}
}
