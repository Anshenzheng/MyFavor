export interface Figure {
  id: number;
  name: string;
  description: string;
  price: number;
  purchaseDate: string;
  userId: number;
  username: string;
  categoryId: number;
  categoryName: string;
  isPublic: boolean;
  viewCount: number;
  likeCount: number;
  wantCount: number;
  primaryImage: string;
  images: string[];
  tagIds: number[];
  tagNames: string[];
  createdAt: string;
  updatedAt: string;
  isLiked: boolean;
  isWanted: boolean;
}

export interface FigureCreateRequest {
  name: string;
  description?: string;
  price?: number;
  purchaseDate?: string;
  categoryId?: number;
  isPublic?: boolean;
  imageUrls?: string[];
  tagNames?: string[];
  tagIds?: number[];
}

export interface FigureUpdateRequest {
  name: string;
  description?: string;
  price?: number;
  purchaseDate?: string;
  categoryId?: number;
  isPublic?: boolean;
  imageUrls?: string[];
  tagNames?: string[];
  tagIds?: number[];
  primaryImageUrl?: string;
}

export interface Category {
  id: number;
  name: string;
  userId: number;
  createdAt: string;
  updatedAt: string;
}

export interface Tag {
  id: number;
  name: string;
  userId: number;
  createdAt: string;
  updatedAt: string;
}

export interface LikeStatus {
  liked: boolean;
  count: number;
}

export interface WantStatus {
  wanted: boolean;
  count: number;
}
