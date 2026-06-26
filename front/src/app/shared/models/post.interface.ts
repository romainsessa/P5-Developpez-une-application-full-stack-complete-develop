import { Comment } from './comment.interface';

export interface Post {
  id: number;
  topic: string;
  title: string;
  content: string;
  author: string;
  createdAt: Date;
  comments: Comment[];
}
