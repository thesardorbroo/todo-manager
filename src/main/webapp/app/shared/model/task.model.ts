import { IComments } from '@/shared/model/comments.model';
import { IGroups } from '@/shared/model/groups.model';

export interface ITask {
  id?: number;
  body?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  caption?: string | null;
  comments?: IComments[] | null;
  groups?: IGroups[] | null;
}

export class Task implements ITask {
  constructor(
    public id?: number,
    public body?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public caption?: string | null,
    public comments?: IComments[] | null,
    public groups?: IGroups[] | null
  ) {}
}
