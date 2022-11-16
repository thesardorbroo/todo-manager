import { ITask } from '@/shared/model/task.model';

export interface IGroups {
  id?: number;
  groupName?: string | null;
  tasks?: ITask[] | null;
}

export class Groups implements IGroups {
  constructor(public id?: number, public groupName?: string | null, public tasks?: ITask[] | null) {}
}
