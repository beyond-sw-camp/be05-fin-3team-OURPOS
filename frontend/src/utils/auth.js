// utils/auth.js

// JWT 토큰 디코딩 함수
export function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    console.log('Decoded Payload:', jsonPayload);
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Failed to parse JWT:', error);
    return null;
  }
}

// 쿠키에서 JWT 토큰을 가져오는 함수
export function getCookie(name) {
  let cookieValue = null;
  if (document.cookie && document.cookie !== '') {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i].trim();
      if (cookie.substring(0, name.length + 1) === (name + '=')) {
        cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
        break;
      }
    }
  }
  console.log(`Cookie [${name}]:`, cookieValue);
  return cookieValue;
}

// 토큰의 만료 여부를 확인하는 함수
export function isTokenExpired(token) {
  const decoded = parseJwt(token);
  if (!decoded || !decoded.exp) {
    console.warn('Invalid JWT or expiration time not found');
    return true; // 만료된 것으로 처리
  }
  const currentTime = Math.floor(Date.now() / 1000); // 현재 시간(UTC)을 초 단위로 변환
  return decoded.exp < currentTime;
}

// 페이지 접근 검증 함수
export function checkUserRole(requiredRoles) {
  const token = getCookie('Authorization');
  if (token) {
    if (isTokenExpired(token)) {
      console.warn('Token expired');
      return false; // 토큰이 만료됨
    }
    const decoded = parseJwt(token);
    if (decoded && decoded.role) {
      console.log('User Role:', decoded.role);
      if (requiredRoles.includes(decoded.role)) {
        console.log('Access Granted');
        return true; // 접근 허용
      } else {
        console.warn(`Access Denied. Required Roles: ${requiredRoles}, User Role: ${decoded.role}`);
        return false; // 접근 거부
      }
    } else {
      console.warn('Invalid JWT payload');
      return false; // JWT 토큰이 없으므로 접근 거부
    }
  } else {
    console.warn('JWT token not found');
    return false; // JWT 토큰이 없으므로 접근 거부
  }
}
