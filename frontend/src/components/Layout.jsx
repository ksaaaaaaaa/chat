import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import styled from 'styled-components';

const Container = styled.div`
  display: flex;
  height: 100vh;
  width: 100vw;
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
`;

const Sidebar = styled.div`
  width: 8%;
  min-width: 80px;
  max-width: 100px;
  background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%);
  display: flex;
  flex-direction: column;
  padding: 20px 10px;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
`;

const Logo = styled.div`
  color: #ecf0f1;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 40px;
  text-align: center;
  padding: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
`;

const NavItem = styled(NavLink)`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 15px 10px;
  margin: 8px 0;
  color: #bdc3c7;
  text-decoration: none;
  border-radius: 10px;
  transition: all 0.3s ease;
  
  &:hover {
    background: rgba(255, 255, 255, 0.1);
    color: #ecf0f1;
  }
  
  &.active {
    background: rgba(52, 152, 219, 0.3);
    color: #ecf0f1;
    border-left: 3px solid #3498db;
  }
`;

const NavIcon = styled.div`
  font-size: 24px;
  margin-bottom: 5px;
`;

const NavText = styled.div`
  font-size: 11px;
  font-weight: 500;
  text-align: center;
`;

const MainContent = styled.div`
  flex: 1;
  width: 92%;
  background-color: #f5f7fa;
  overflow-y: auto;
  padding: 30px;
`;

const Layout = ({ children }) => {
  const location = useLocation();

  const navItems = [
    {
      path: '/topics',
      icon: '💬',
      label: '话题区'
    },
    {
      path: '/videos',
      icon: '🎬',
      label: '视频区'
    },
    {
      path: '/user',
      icon: '👤',
      label: '用户信息'
    },
    {
      path: '/friends',
      icon: '👥',
      label: '好友列表'
    }
  ];

  return (
    <Container>
      <Sidebar>
        <Logo>社交平台</Logo>
        {navItems.map((item) => (
          <NavItem
            key={item.path}
            to={item.path}
            className={location.pathname === item.path ? 'active' : ''}
          >
            <NavIcon>{item.icon}</NavIcon>
            <NavText>{item.label}</NavText>
          </NavItem>
        ))}
      </Sidebar>
      <MainContent>
        {children}
      </MainContent>
    </Container>
  );
};

export default Layout;
