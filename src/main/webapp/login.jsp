<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% 
    // Redirect to dashboard if already logged in
    if(session.getAttribute("username") != null) {
        response.sendRedirect("dashboard.jsp");
        return;
    }
%>

<jsp:include page="includes/header.jsp" />

<div class="flex-grow flex items-center justify-center bg-gradient-to-br from-blue-50 to-gray-200">
    <div class="bg-white p-10 rounded-2xl shadow-2xl w-full max-w-md transform transition-all duration-300 hover:shadow-blue-500/20">
        
        <div class="text-center mb-10">
            <h1 class="text-3xl font-bold text-gray-800 mb-2">Welcome Back</h1>
            <p class="text-gray-500">Sign in to the Sunrise Dental System</p>
        </div>

        <%-- Error Message Container --%>
        <% if(request.getAttribute("error") != null) { %>
            <div class="bg-red-50 border-l-4 border-red-500 p-4 mb-6 rounded-md">
                <div class="flex items-center">
                    <svg class="h-5 w-5 text-red-500 mr-2" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd"/>
                    </svg>
                    <p class="text-sm text-red-700 font-medium"><%= request.getAttribute("error") %></p>
                </div>
            </div>
        <% } %>

        <form action="login" method="post" class="space-y-6">
            <input type="hidden" name="action" value="login">
            
            <div>
                <label for="username" class="block text-sm font-medium text-gray-700">Username</label>
                <div class="mt-1">
                    <input id="username" name="username" type="text" required 
                        class="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition" 
                        placeholder="Enter your username">
                </div>
            </div>

            <div>
                <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
                <div class="mt-1">
                    <input id="password" name="password" type="password" required 
                        class="appearance-none block w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition" 
                        placeholder="••••••••">
                </div>
            </div>

            <div class="pt-2">
                <button type="submit" 
                    class="w-full flex justify-center py-3 px-4 border border-transparent rounded-lg shadow-md text-sm font-bold text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 btn-hover">
                    Sign In to Dashboard
                </button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="includes/footer.jsp" />
