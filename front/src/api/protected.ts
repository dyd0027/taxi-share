// pages/api/protected.ts

import { NextApiRequest, NextApiResponse } from 'next';
import { parse } from 'cookie';
import jwt from 'jsonwebtoken';

export default async (req: NextApiRequest, res: NextApiResponse) => {
  const { token } = parse(req.headers.cookie || '');

  if (!token) {
    return res.status(401).json({ message: 'Authentication required' });
  }

  try {
    const decoded = jwt.verify(token, 'taxi_share');
    // 보호된 리소스에 접근
    res.status(200).json({ message: 'Protected content', data: decoded });
  } catch (error) {
    return res.status(401).json({ message: 'Invalid token' });
  }
};