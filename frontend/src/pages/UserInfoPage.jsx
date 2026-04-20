import React, { useState, useEffect } from 'react';
import styled from 'styled-components';

const PageContainer = styled.div`
  max-width: 800px;
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

const ProfileCard = styled.div`
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
`;

const ProfileHeader = styled.div`
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60px 40px 40px;
  position: relative;
`;

const AvatarContainer = styled.div`
  position: absolute;
  bottom: -60px;
  left: 40px;
`;

const Avatar = styled.img`
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 4px solid white;
  object-fit: cover;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  background: #ecf0f1;
`;

const ProfileBody = styled.div`
  padding: 80px 40px 40px;
`;

const UserName = styled.h2`
  font-size: 24px;
  color: #2c3e50;
  margin: 0 0 8px 0;
  font-weight: 600;
`;

const UserRole = styled.div`
  display: inline-block;
  background: #e3f2fd;
  color: #1976d2;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  margin-bottom: 16px;
`;

const UserBio = styled.p`
  font-size: 15px;
  color: #34495e;
  line-height: 1.6;
  margin: 0 0 24px 0;
`;

const StatsRow = styled.div`
  display: flex;
  gap: 40px;
  margin-bottom: 32px;
`;

const StatItem = styled.div`
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s ease;
  
  &:hover {
    transform: translateY(-2px);
  }
`;

const StatValue = styled.div`
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 4px;
`;

const StatLabel = styled.div`
  font-size: 14px;
  color: #7f8c8d;
  font-weight: 500;
`;

const Section = styled.div`
  margin-bottom: 32px;
  
  &:last-child {
    margin-bottom: 0;
  }
`;

const SectionTitle = styled.h3`
  font-size: 18px;
  color: #2c3e50;
  margin: 0 0 16px 0;
  font-weight: 600;
  padding-bottom: 12px;
  border-bottom: 1px solid #ecf0f1;
`;

const InfoGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
`;

const InfoItem = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
`;

const InfoIcon = styled.span`
  font-size: 20px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
  border-radius: 10px;
`;

const InfoContent = styled.div`
  flex: 1;
`;

const InfoLabel = styled.div`
  font-size: 12px;
  color: #95a5a6;
  margin-bottom: 2px;
`;

const InfoValue = styled.div`
  font-size: 14px;
  color: #2c3e50;
  font-weight: 500;
`;

const EditButton = styled.button`
  position: absolute;
  top: 20px;
  right: 20px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  
  &:hover {
    background: rgba(255, 255, 255, 0.3);
  }
`;

const mockUser = {
  id: 1,
  name: '张小明',
  avatar: 'https://i.pravatar.cc/150?img=10',
  role: '前端开发工程师',
  bio: '热爱技术，喜欢分享。专注于前端开发，React/TypeScript 爱好者。欢迎交流学习！',
  email: 'zhangxiaoming@example.com',
  phone: '138****8888',
  location: '北京市',
  joinDate: '2023-01-15',
  stats: {
    topics: 128,
    videos: 45,
    followers: 1256,
    following: 89
  }
};

const UserInfoPage = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setUser(mockUser);
      setLoading(false);
    }, 500);

    return () => clearTimeout(timer);
  }, []);

  if (loading || !user) {
    return (
      <PageContainer>
        <PageHeader>
          <PageTitle>用户信息</PageTitle>
          <PageSubtitle>加载中...</PageSubtitle>
        </PageHeader>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <PageHeader>
        <PageTitle>用户信息</PageTitle>
        <PageSubtitle>查看和管理您的个人信息</PageSubtitle>
      </PageHeader>
      
      <ProfileCard>
        <ProfileHeader>
          <EditButton>编辑资料</EditButton>
          <AvatarContainer>
            <Avatar src={user.avatar} alt={user.name} />
          </AvatarContainer>
        </ProfileHeader>
        
        <ProfileBody>
          <UserName>{user.name}</UserName>
          <UserRole>{user.role}</UserRole>
          <UserBio>{user.bio}</UserBio>
          
          <StatsRow>
            <StatItem>
              <StatValue>{user.stats.topics}</StatValue>
              <StatLabel>话题</StatLabel>
            </StatItem>
            <StatItem>
              <StatValue>{user.stats.videos}</StatValue>
              <StatLabel>视频</StatLabel>
            </StatItem>
            <StatItem>
              <StatValue>{user.stats.followers}</StatValue>
              <StatLabel>粉丝</StatLabel>
            </StatItem>
            <StatItem>
              <StatValue>{user.stats.following}</StatValue>
              <StatLabel>关注</StatLabel>
            </StatItem>
          </StatsRow>
          
          <Section>
            <SectionTitle>基本信息</SectionTitle>
            <InfoGrid>
              <InfoItem>
                <InfoIcon>📧</InfoIcon>
                <InfoContent>
                  <InfoLabel>邮箱</InfoLabel>
                  <InfoValue>{user.email}</InfoValue>
                </InfoContent>
              </InfoItem>
              <InfoItem>
                <InfoIcon>📱</InfoIcon>
                <InfoContent>
                  <InfoLabel>电话</InfoLabel>
                  <InfoValue>{user.phone}</InfoValue>
                </InfoContent>
              </InfoItem>
              <InfoItem>
                <InfoIcon>📍</InfoIcon>
                <InfoContent>
                  <InfoLabel>所在地</InfoLabel>
                  <InfoValue>{user.location}</InfoValue>
                </InfoContent>
              </InfoItem>
              <InfoItem>
                <InfoIcon>📅</InfoIcon>
                <InfoContent>
                  <InfoLabel>加入时间</InfoLabel>
                  <InfoValue>{user.joinDate}</InfoValue>
                </InfoContent>
              </InfoItem>
            </InfoGrid>
          </Section>
        </ProfileBody>
      </ProfileCard>
    </PageContainer>
  );
};

export default UserInfoPage;
