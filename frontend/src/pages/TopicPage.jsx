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

const TopicCard = styled.div`
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;
  
  &:hover {
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
`;

const TopicHeader = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 16px;
`;

const Avatar = styled.img`
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 12px;
  background: #ecf0f1;
`;

const UserInfo = styled.div`
  flex: 1;
`;

const UserName = styled.div`
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 4px;
`;

const PostTime = styled.div`
  font-size: 13px;
  color: #95a5a6;
`;

const TopicTitle = styled.h3`
  font-size: 18px;
  color: #2c3e50;
  margin: 0 0 12px 0;
  font-weight: 600;
`;

const TopicContent = styled.p`
  font-size: 15px;
  color: #34495e;
  line-height: 1.6;
  margin: 0 0 16px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
`;

const TopicTags = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
`;

const Tag = styled.span`
  background: #e3f2fd;
  color: #1976d2;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 13px;
`;

const TopicActions = styled.div`
  display: flex;
  gap: 24px;
  padding-top: 16px;
  border-top: 1px solid #ecf0f1;
`;

const ActionItem = styled.div`
  display: flex;
  align-items: center;
  gap: 6px;
  color: #7f8c8d;
  font-size: 14px;
  cursor: pointer;
  transition: color 0.3s ease;
  
  &:hover {
    color: #3498db;
  }
`;

const ActionIcon = styled.span`
  font-size: 18px;
`;

const mockTopics = [
  {
    id: 1,
    title: '如何高效学习编程？分享我的学习方法',
    content: '学习编程是一个需要持续投入的过程，我在这里分享一下我的学习经验和方法，希望能够帮助到正在学习编程的朋友们。首先，要建立良好的学习习惯，每天保持一定的学习时间...',
    user: {
      name: '张三',
      avatar: 'https://i.pravatar.cc/150?img=1'
    },
    tags: ['编程', '学习方法', '经验分享'],
    postTime: '2024-01-15 10:30',
    likes: 128,
    comments: 45,
    views: 1256
  },
  {
    id: 2,
    title: 'React Hooks 最佳实践指南',
    content: 'React Hooks 已经成为 React 开发的标准方式，本文将深入探讨如何正确使用 Hooks，避免常见的陷阱。从 useState 到 useEffect，再到自定义 Hook，让我们一起深入了解...',
    user: {
      name: '李四',
      avatar: 'https://i.pravatar.cc/150?img=2'
    },
    tags: ['React', 'Hooks', '前端开发'],
    postTime: '2024-01-14 15:20',
    likes: 256,
    comments: 89,
    views: 3421
  },
  {
    id: 3,
    title: '2024年前端开发趋势预测',
    content: '新的一年即将到来，让我们一起预测一下2024年前端开发的趋势。从 AI 辅助编程到新兴框架，从性能优化到用户体验，前端领域正在快速发展...',
    user: {
      name: '王五',
      avatar: 'https://i.pravatar.cc/150?img=3'
    },
    tags: ['前端', '趋势', '2024'],
    postTime: '2024-01-13 09:15',
    likes: 189,
    comments: 67,
    views: 2156
  }
];

const TopicPage = () => {
  const [topics, setTopics] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setTopics(mockTopics);
      setLoading(false);
    }, 500);

    return () => clearTimeout(timer);
  }, []);

  if (loading) {
    return (
      <PageContainer>
        <PageHeader>
          <PageTitle>话题区</PageTitle>
          <PageSubtitle>加载中...</PageSubtitle>
        </PageHeader>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <PageHeader>
        <PageTitle>话题区</PageTitle>
        <PageSubtitle>浏览和参与社区热门话题讨论</PageSubtitle>
      </PageHeader>
      
      {topics.map((topic) => (
        <TopicCard key={topic.id}>
          <TopicHeader>
            <Avatar src={topic.user.avatar} alt={topic.user.name} />
            <UserInfo>
              <UserName>{topic.user.name}</UserName>
              <PostTime>{topic.postTime}</PostTime>
            </UserInfo>
          </TopicHeader>
          
          <TopicTitle>{topic.title}</TopicTitle>
          <TopicContent>{topic.content}</TopicContent>
          
          <TopicTags>
            {topic.tags.map((tag, index) => (
              <Tag key={index}>{tag}</Tag>
            ))}
          </TopicTags>
          
          <TopicActions>
            <ActionItem>
              <ActionIcon>👍</ActionIcon>
              <span>{topic.likes} 点赞</span>
            </ActionItem>
            <ActionItem>
              <ActionIcon>💬</ActionIcon>
              <span>{topic.comments} 评论</span>
            </ActionItem>
            <ActionItem>
              <ActionIcon>👁️</ActionIcon>
              <span>{topic.views} 浏览</span>
            </ActionItem>
          </TopicActions>
        </TopicCard>
      ))}
    </PageContainer>
  );
};

export default TopicPage;
