<!-- Menu bar -->
<div class="ui menu">
    <div class="header item">
        <a href="editor" id="homeLink">Quick Notes</a>
    </div>
    <div class="right menu" id="rightMenu">
        <a href="#" class="item" id="signIn">Sign In</a>
        <a href="#" class="item">Sign Up</a>
    </div>

    <input id="cxt" type="hidden" value="<%=request.getContextPath()%>">
</div>

<!-- Login modal -->
<div class="ui tiny modal" id="loginModal">

    <i class="icon-cancel" id="loginExit"></i>
    <div class="header">
        Sign In
    </div>

    <div class="content">

        <form class="ui form" id="loginForm">

            <div class="field" id="emailDiv">
                <label>E-mail</label>
                <input type="text" id="email" name="email">
            </div>

            <div class="field" id="passDiv">
                <label>Password</label>
                <input type="password" id="password" name="password">
                <div class="ui compact small negative message compactPadding" id="loginError" style="display: none;">
                    <p>Email and password must not be left blank</p>
                </div>
            </div>

        </form>

        <button class="ui small button" id="loginBtn">Sign In</button>

    </div>
</div>

<!-- Sign up modal -->
<div class="ui tiny modal" id="signUpModal">

    <i class="icon-cancel" id="signUpExit"></i>
    <div class="header">Sign Up</div>

    <div class="content">

        <form class="ui form" id="signUpForm">

            <div class="field" id="signUpEmailDiv">
                <label>Email</label>
                <input type="text" id="signUpEmail">
                <div class="ui compact small negative message compactPadding" id="signUpEmailErr" style="display: none;"></div>
            </div>

            <div class="field" id="signUpUNameDiv">
                <label>Username</label>
                <input type="text" id="signUpUName">
                <div class="ui compact small negative message compactPadding" id="signUpUNameErr" style="display: none;"></div>
            </div>

            <div class="field" id="signUpPassDiv">
                <label>Password</label>
                <input type="password" id="signUpPass">
                <div class="ui compact small negative message compactPadding" id="signUpPassErr" style="display: none;"></div>
            </div>

            <div class="field" id="signUpPassDiv2">
                <label>Password (Re-enter)</label>
                <input type="password" id="signUpPass2">
                <div class="ui compact small negative message compactPadding" id="signUpPass2OrAllErr" style="display: none;"></div>
            </div>

        </form>

        <br>
        <button class="ui button right floated small green leftButtonMargin" id="signUpBtn">Sign Up</button>
        <button class="ui button right floated small" id="signUpCancel">Cancel</button>
        <br><br>

    </div>

</div>

<div class="ui tiny modal" id="regConfirmModal">

    <i class="icon-cancel" id="exitRegConfirm"></i>
    <div class="header" id="actTitle">
        Check your Inbox
    </div>

    <div class="content">
        <p id="actMsg">
            Your account has been created and an email containing an activation link has been sent
            to your inbox, if you did not receive an email click here to resend.
        </p>
    </div>

    <button class="ui button" id="resendActivation">Resend Email</button>
    <br>
</div>

<!-- Create modal -->
<div class="ui tiny modal" id="createModal">

    <i class="icon-cancel" id="createExit"></i>
    <div class="header">
        Create
    </div>

    <div class="content">

        <form class="ui form">

            <div class="field" id="titleDiv">
                <label>Title</label>
                <input id="title" type="text" maxlength="40">
                <div class="ui compact small negative message compactPadding" id="titleError" style="display: none;">
                    <p>Note must have title</p>
                </div>
            </div>

        </form>

        <br>
        <button class="ui right floated green small button leftButtonMargin" id="createSubmit">Create</button>
        <button class="ui right floated small button" id="createCancel">Cancel</button>
        <br><br>

    </div>
</div>

<!-- Translate modal -->
<div class="ui tiny modal" id="translateModal">

    <i class="icon-cancel" id="translateExit"></i>
    <div class="header">
        Translate
    </div>

    <div class="content">

        <form class="ui form">

            <div class="field">
                <label>Source Language</label>
                <select id="sourceLang" class="ui fluid dropdown">
                    <%@include file="langDropdown.jsp"%>
                </select>
            </div>

            <div class="field">
                <label>Destination Language</label>
                <select id="destLang" class="ui fluid dropdown">
                    <%@include file="langDropdownDest.jsp"%>
                </select>
            </div>

        </form>

        <br>
        <button class="ui right floated green small button leftButtonMargin" id="translateSubmit">Translate</button>
        <button class="ui right floated small button" id="translateCancel">Cancel</button>
        <br><br>

    </div>

</div>

