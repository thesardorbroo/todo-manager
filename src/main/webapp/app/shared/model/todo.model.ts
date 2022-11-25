import { ITask } from '@/shared/model/task.model';
import { ICustomer } from '@/shared/model/customer.model';

export interface ITodo {
  id?: number;
  createdAt?: Date | null;
  none?: boolean | null;
  task?: ITask | null;
  customer?: ICustomer | null;
}

export class Todo implements ITodo {
  constructor(
    public id?: number,
    public createdAt?: Date | null,
    public none?: boolean | null,
    public task?: ITask | null,
    public customer?: ICustomer | null
  ) {
    this.none = this.none ?? false;
  }
}
