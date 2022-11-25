export interface ITodoList {
  id?: number;
  createdAt?: Date | null;
  none?: boolean | null;
}

export class TodoList implements ITodoList {
  constructor(public id?: number, public createdAt?: Date | null, public none?: boolean | null) {
    this.none = this.none ?? false;
  }
}
