import React, { useState, useEffect } from 'react';
import styled from 'styled-components';

const PageContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
`;

const PageHeader = styled.div`
  margin-bottom: 30px;
`;

const PageTitle = styled.h1`
  font-size: 28px;
  color: #2c3e50;
  margin: 0 0 10px 0;
  font-weight: 600;
`;

const PageSubtitle = styled.p`
  font-size: 14px;
  color: #7f8c8d;
  margin: 0;
`;

const VideoGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
`;

const VideoCard = styled.div`
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;
  
  &:hover {
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    transform: translateY(-4px);
  }
`;

const VideoThumbnail = styled.div`
  position: relative;
  width: 100%;
  height: 160px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const VideoDuration = styled.span`
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
`;

const PlayIcon = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  opacity: 0;
  transition: opacity 0.3s ease;
  
  ${VideoCard}:hover & {
    opacity: 1;
  }
`;

const VideoInfo = styled.div`
  padding: 16px;
`;

const VideoTitle = styled.h3`
  font-size: 16px;
  color: #2c3e50;
  margin: 0 0 12px 0;
  font-weight: 600;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
`;

const VideoMeta = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
`;

const AuthorAvatar = styled.img`
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  background: #ecf0f1;
`;

const AuthorName = styled.span`
  font-size: 14px;
  color: #7f8c8d;
  font-weight: 500;
`;

const VideoStats = styled.div`
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #95a5a6;
`;

const StatItem = styled.div`
  display: flex;
  align-items: center;
  gap: 4px;
`;

const mockVideos = [
  {
    id: 1,
    title: 'React 18 新特性详解：并发渲染与自动批处理',
    thumbnail: 'https://picsum.photos/seed/video1/400/250',
    duration: '12:35',
    author: {
      name: '前端小王',
      avatar: 'https://i.pravatar.cc/150?img=4'
    },
    views: 12560,
    likes: 890,
    uploadTime: '3天前'
  },
  {
    id: 2,
    title: 'TypeScript 高级类型技巧与实战',
    thumbnail: 'https://picsum.photos/seed/video2/400/250',
    duration: '25:18',
    author: {
      name: 'TS大神',
      avatar: 'https://i.pravatar.cc/150?img=5'
    },
    views: 28900,
    likes: 2340,
    uploadTime: '1周前'
  },
  {
    id: 3,
    title: 'Node.js 性能优化最佳实践',
    thumbnail: 'https://picsum.photos/seed/video3/400/250',
    duration: '18:45',
    author: {
      name: '后端老司机',
      avatar: 'https://i.pravatar.cc/150?img=6'
    },
    views: 15600,
    likes: 1230,
    uploadTime: '2周前'
  },
  {
    id: 4,
    title: 'CSS Grid 布局完全指南',
    thumbnail: 'https://picsum.photos/seed/video4/400/250',
    duration: '32:10',
    author: {
      name: 'CSS大师',
      avatar: 'https://i.pravatar.cc/150?img=7'
    },
    views: 45200,
    likes: 3450,
    uploadTime: '1个月前'
  },
  {
    id: 5,
    title: 'Next.js 14 App Router 深度解析',
    thumbnail: 'https://picsum.photos/seed/video5/400/250',
    duration: '28:55',
    author: {
      name: 'React专家',
      avatar: 'https://i.pravatar.cc/150?img=8'
    },
    views: 21300,
    likes: 1890,
    uploadTime: '5天前'
  },
  {
    id: 6,
    title: 'Web性能优化：从入门到精通',
    thumbnail: 'https://picsum.photos/seed/video6/400/250',
    duration: '45:20',
    author: {
      name: '性能优化师',
      avatar: 'https://i.pravatar.cc/150?img=9'
    },
    views: 67800,
    likes: 5670,
    uploadTime: '2周前'
  }
];

const VideoPage = () => {
  const [videos, setVideos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setVideos(mockVideos);
      setLoading(false);
    }, 500);

    return () => clearTimeout(timer);
  }, []);

  if (loading) {
    return (
      <PageContainer>
        <PageHeader>
          <PageTitle>视频区</PageTitle>
          <PageSubtitle>加载中...</PageSubtitle>
        </PageHeader>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <PageHeader>
        <PageTitle>视频区</PageTitle>
        <PageSubtitle>发现精彩视频内容，学习新知识</PageSubtitle>
      </PageHeader>
      
      <VideoGrid>
        {videos.map((video) => (
          <VideoCard key={video.id}>
            <VideoThumbnail>
              <img src={video.thumbnail} alt={video.title} />
              <VideoDuration>{video.duration}</VideoDuration>
              <PlayIcon>▶</PlayIcon>
            </VideoThumbnail>
            <VideoInfo>
              <VideoTitle>{video.title}</VideoTitle>
              <VideoMeta>
                <AuthorAvatar src={video.author.avatar} alt={video.author.name} />
                <AuthorName>{video.author.name}</AuthorName>
              </VideoMeta>
              <VideoStats>
                <StatItem>
                  <span>👁️</span>
                  <span>{video.views.toLocaleString()}</span>
                </StatItem>
                <StatItem>
                  <span>👍</span>
                  <span>{video.likes.toLocaleString()}</span>
                </StatItem>
                <StatItem>
                  <span>📅</span>
                  <span>{video.uploadTime}</span>
                </StatItem>
              </VideoStats>
            </VideoInfo>
          </VideoCard>
        ))}
      </VideoGrid>
    </PageContainer>
  );
};

export default VideoPage;
