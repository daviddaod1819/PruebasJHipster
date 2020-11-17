export interface ISpace {
  id?: number;
  rooms?: number;
  meters?: number;
  price?: number;
  details?: string;
  userInfoId?: number;
}

export class Space implements ISpace {
  constructor(
    public id?: number,
    public rooms?: number,
    public meters?: number,
    public price?: number,
    public details?: string,
    public userInfoId?: number
  ) {}
}
