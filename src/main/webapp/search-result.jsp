<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<% 
    // Session Check
    if(session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<jsp:include page="includes/header.jsp" />

<div class="flex-grow flex bg-gray-50">
    <%-- Sidebar Menu --%>
    <aside class="w-64 bg-white border-r border-gray-200 shadow-sm hidden md:block">
        <div class="h-full px-3 py-6 overflow-y-auto">
            <ul class="space-y-2 font-medium">
                <li>
                    <a href="dashboard" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
                        <span class="mr-3 text-xl">📊</span>
                        <span>Dashboard Overview</span>
                    </a>
                </li>
                <li>
                    <a href="register-appointment.jsp" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
                        <span class="mr-3 text-xl">📝</span>
                        <span>Register Appointment</span>
                    </a>
                </li>
                <li>
                    <a href="billing" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
                        <span class="mr-3 text-xl">💳</span>
                        <span>Billing & Invoices</span>
                    </a>
                </li>
                <li>
                    <a href="help.jsp" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
                        <span class="mr-3 text-xl">❓</span>
                        <span>Help Center</span>
                    </a>
                </li>
            </ul>
        </div>
    </aside>

    <div class="flex-1 p-8">
        <div class="max-w-4xl mx-auto">
            <div class="flex justify-between items-center mb-6">
                <h2 class="text-3xl font-bold text-gray-800">Search Results</h2>
                <a href="dashboard" class="text-blue-600 hover:underline">← Back to Dashboard</a>
            </div>

            <c:if test="${not empty error}">
                <div class="bg-red-50 border-l-4 border-red-500 p-4 mb-6 rounded-md shadow-sm">
                    <p class="text-red-700 font-medium"><c:out value="${error}" /></p>
                </div>
            </c:if>

            <c:if test="${not empty appointment}">
                <div class="bg-white rounded-xl shadow-md border border-gray-100 p-8">
                    <div class="border-b pb-4 mb-4">
                        <h3 class="text-2xl font-bold text-blue-800">Appointment <c:out value="${appointment.appointmentNumber}" /></h3>
                        <p class="text-sm text-gray-500 mt-1">Status: <span class="bg-green-100 text-green-800 text-xs font-semibold px-2.5 py-0.5 rounded"><c:out value="${appointment.status}" /></span></p>
                    </div>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
                        <div>
                            <h4 class="text-lg font-semibold text-gray-700 border-b pb-2 mb-3">Patient Details</h4>
                            <p class="text-gray-600"><span class="font-medium text-gray-800">Name:</span> <c:out value="${appointment.patient.patientName}" /></p>
                            <p class="text-gray-600"><span class="font-medium text-gray-800">Contact:</span> <c:out value="${appointment.patient.contactNumber}" /></p>
                        </div>
                        <div>
                            <h4 class="text-lg font-semibold text-gray-700 border-b pb-2 mb-3">Appointment Details</h4>
                            <p class="text-gray-600"><span class="font-medium text-gray-800">Dentist:</span> <c:out value="${appointment.dentistName}" /></p>
                            <p class="text-gray-600"><span class="font-medium text-gray-800">Treatment ID:</span> <c:out value="${appointment.treatment.treatmentId}" /></p>
                            <p class="text-gray-600"><span class="font-medium text-gray-800">Date:</span> <c:out value="${appointment.appointmentDate}" /></p>
                            <p class="text-gray-600"><span class="font-medium text-gray-800">Time:</span> <c:out value="${appointment.appointmentTime}" /></p>
                        </div>
                    </div>

                    <div class="mt-8 flex justify-end">
                        <form action="billing" method="post">
                            <input type="hidden" name="appointmentNumber" value="<c:out value='${appointment.appointmentNumber}' />" />
                            <button type="submit" class="bg-gray-800 text-white px-6 py-2 rounded-lg hover:bg-gray-900 transition shadow">Generate Bill</button>
                        </form>
                    </div>
                </div>
            </c:if>

        </div>
    </div>
</div>

<jsp:include page="includes/footer.jsp" />
