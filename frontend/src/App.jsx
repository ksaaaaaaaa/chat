import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import TopicPage from './pages/TopicPage';
import VideoPage from './pages/VideoPage';
import UserInfoPage from './pages/UserInfoPage';
import FriendsListPage from './pages/FriendsListPage';

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Navigate to="/topics" replace />} />
          <Route path="/topics" element={<TopicPage />} />
          <Route path="/videos" element={<VideoPage />} />
          <Route path="/user" element={<UserInfoPage />} />
          <Route path="/friends" element={<FriendsListPage />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
