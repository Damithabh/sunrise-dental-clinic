<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sunrise Dental Clinic</title>
    <!-- Tailwind CSS (via CDN for modern, stunning UI) -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #f3f4f6; }
        /* Micro-animations */
        .btn-hover { transition: transform 0.2s ease, box-shadow 0.2s ease; }
        .btn-hover:hover { transform: translateY(-2px); box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }
    </style>
</head>
<body class="text-gray-800 antialiased min-h-screen flex flex-col">

<%-- Navigation Bar (Only show if logged in) --%>
<% if (session.getAttribute("username") != null) { %>
    <nav class="bg-blue-800 text-white shadow-lg sticky top-0 z-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex items-center justify-between h-16">
                <div class="flex items-center gap-3">
                    <span class="text-2xl">🦷</span>
                    <span class="font-bold text-xl tracking-wide">Sunrise Dental</span>
                </div>
                <div class="hidden md:block">
                    <div class="ml-10 flex items-baseline space-x-4">
                        <a href="dashboard.jsp" class="hover:bg-blue-700 px-3 py-2 rounded-md text-sm font-medium transition">Dashboard</a>
                        <a href="register-appointment.jsp" class="hover:bg-blue-700 px-3 py-2 rounded-md text-sm font-medium transition">Register Appointment</a>
                        <a href="billing.jsp" class="hover:bg-blue-700 px-3 py-2 rounded-md text-sm font-medium transition">Billing</a>
                        
                        <form action="login" method="post" class="inline ml-4 border-l border-blue-600 pl-4">
                            <input type="hidden" name="action" value="logout">
                            <span class="text-blue-200 text-sm mr-4">Welcome, <%= session.getAttribute("username") %></span>
                            <button type="submit" class="bg-red-500 hover:bg-red-600 px-4 py-2 rounded-md text-sm font-medium btn-hover">Logout</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </nav>
<% } %>

<!-- Main Content Wrapper -->
<main class="flex-grow flex flex-col w-full">
