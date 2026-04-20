import React, { useState, useEffect } from 'react';
import styled from 'styled-components';

const PageContainer = styled.div`
  max-width: 900px;
  margin: 0 auto;
`;

const PageHeader = styled.div`
  margin-bottom: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const HeaderLeft = styled.div``;

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

const SearchBox = styled.div`
  position: relative;
  width: 280px;
`;

const SearchInput = styled.input`
  width: 100%;
  padding: 12px 16px 12px 44px;
  border: 1px solid #e0e0e0;
  border-radius: 25px;
  font-size: 14px;
  transition: all 0.3s ease;
  
  &:focus {
    outline: none;
    border-color: #3498db;
    box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
  }
  
  &::placeholder {
    color: #bdc3c7;
  }
`;

const SearchIcon = styled.span`
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 18px;
  color: #95a5a6;
`;

const FriendsList = styled.div`
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
`;

const SectionHeader = styled.div`
  padding: 20px 24px;
  background: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const SectionTitle = styled.h3`
  font-size: 16px;
  color: #2c3e50;
  margin: 0;
  font-weight: 600;
`;

const FriendCount = styled.span`
  font-size: 13px;
  color: #7f8c8d;
  font-weight: 500;
`;

const FriendItem = styled.div`
  display: flex;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.2s ease;
  cursor: pointer;
  
  &:hover {
    background-color: #f8f9fa;
  }
  
  &:last-child {
    border-bottom: none;
  }
`;

const Avatar = styled.img`
  width: 56px;
  height: 56px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 16px;
  background: #ecf0f1;
  position: relative;
`;

const OnlineStatus = styled.div`
  position: absolute;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: ${props => props.online ? '#2ecc71' : '#95a5a6'};
  border: 2px solid white;
  bottom: 4px;
  right: 4px;
`;

const AvatarWrapper = styled.div`
  position: relative;
`;

const FriendInfo = styled.div`
  flex: 1;
`;

const FriendName = styled.div`
  font-size: 16px;
  color: #2c3e50;
  font-weight: 600;
  margin-bottom: 4px;
`;

const FriendStatus = styled.div`
  font-size: 13px;
  color: ${props => props.online ? '#2ecc71' : '#95a5a6'};
  display: flex;
  align-items: center;
  gap: 6px;
`;

const StatusDot = styled.span`
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: ${props => props.online ? '#2ecc71' : '#95a5a6'};
`;

const FriendActions = styled.div`
  display: flex;
  gap: 12px;
`;

const ActionButton = styled.button`
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  
  &.primary {
    background: #3498db;
    color: white;
    
    &:hover {
      background: #2980b9;
    }
  }
  
  &.secondary {
    background: #f1f2f6;
    color: #2c3e50;
    
    &:hover {
      background: #e9ecef;
    }
  }
`;

const EmptyState = styled.div`
  text-align: center;
  padding: 60px 20px;
  color: #95a5a6;
`;

const EmptyIcon = styled.div`
  font-size: 64px;
  margin-bottom: 16px;
`;

const EmptyText = styled.p`
  font-size: 16px;
  margin: 0;
`;

const mockFriends = [
  {
    id: 1,
    name: '李华',
    avatar: 'https://i.pravatar.cc/150?img=11',
    online: true,
    status: '在线'
  },
  {
    id: 2,
    name: '王芳',
    avatar: 'https://i.pravatar.cc/150?img=12',
    online: true,
    status: '在线 - 正在看视频'
  },
  {
    id: 3,
    name: '张伟',
    avatar: 'https://i.pravatar.cc/150?img=13',
    online: false,
    status: '离线 - 3小时前活跃'
  },
  {
    id: 4,
    name: '刘洋',
    avatar: 'https://i.pravatar.cc/150?img=14',
    online: true,
    status: '在线 - 正在写话题'
  },
  {
    id: 5,
    name: '陈静',
    avatar: 'https://i.pravatar.cc/150?img=15',
    online: false,
    status: '离线 - 1天前活跃'
  },
  {
    id: 6,
    name: '赵强',
    avatar: 'https://i.pravatar.cc/150?img=16',
    online: true,
    status: '在线'
  }
];

const FriendsListPage = () => {
  const [friends, setFriends] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setFriends(mockFriends);
      setLoading(false);
    }, 500);

    return () => clearTimeout(timer);
  }, []);

  const filteredFriends = friends.filter(friend => 
    friend.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const onlineFriends = filteredFriends.filter(friend => friend.online);
  const offlineFriends = filteredFriends.filter(friend => !friend.online);

  if (loading) {
    return (
      <PageContainer>
        <PageHeader>
          <HeaderLeft>
            <PageTitle>好友列表</PageTitle>
            <PageSubtitle>加载中...</PageSubtitle>
          </HeaderLeft>
        </PageHeader>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <PageHeader>
        <HeaderLeft>
          <PageTitle>好友列表</PageTitle>
          <PageSubtitle>管理您的好友和查看他们的动态</PageSubtitle>
        </HeaderLeft>
        <SearchBox>
          <SearchIcon>🔍</SearchIcon>
          <SearchInput 
            type="text" 
            placeholder="搜索好友..." 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </SearchBox>
      </PageHeader>
      
      <FriendsList>
        {onlineFriends.length > 0 && (
          <>
            <SectionHeader>
              <SectionTitle>在线好友</SectionTitle>
              <FriendCount>{onlineFriends.length} 位在线</FriendCount>
            </SectionHeader>
            {onlineFriends.map((friend) => (
              <FriendItem key={friend.id}>
                <AvatarWrapper>
                  <Avatar src={friend.avatar} alt={friend.name} />
                  <OnlineStatus online={friend.online} />
                </AvatarWrapper>
                <FriendInfo>
                  <FriendName>{friend.name}</FriendName>
                  <FriendStatus online={friend.online}>
                    <StatusDot online={friend.online} />
                    {friend.status}
                  </FriendStatus>
                </FriendInfo>
                <FriendActions>
                  <ActionButton className="primary">发消息</ActionButton>
                  <ActionButton className="secondary">查看资料</ActionButton>
                </FriendActions>
              </FriendItem>
            ))}
          </>
        )}
        
        {offlineFriends.length > 0 && (
          <>
            <SectionHeader>
              <SectionTitle>离线好友</SectionTitle>
              <FriendCount>{offlineFriends.length} 位离线</FriendCount>
            </SectionHeader>
            {offlineFriends.map((friend) => (
              <FriendItem key={friend.id}>
                <AvatarWrapper>
                  <Avatar src={friend.avatar} alt={friend.name} />
                  <OnlineStatus online={friend.online} />
                </AvatarWrapper>
                <FriendInfo>
                  <FriendName>{friend.name}</FriendName>
                  <FriendStatus online={friend.online}>
                    <StatusDot online={friend.online} />
                    {friend.status}
                  </FriendStatus>
                </FriendInfo>
                <FriendActions>
                  <ActionButton className="primary">发消息</ActionButton>
                  <ActionButton className="secondary">查看资料</ActionButton>
                </FriendActions>
              </FriendItem>
            ))}
          </>
        )}
        
        {filteredFriends.length === 0 && (
          <EmptyState>
            <EmptyIcon>👥</EmptyIcon>
            <EmptyText>没有找到匹配的好友</EmptyText>
          </EmptyState>
        )}
      </FriendsList>
    </PageContainer>
  );
};

export default FriendsListPage;
